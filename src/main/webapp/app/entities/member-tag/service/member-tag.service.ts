import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMemberTag, NewMemberTag } from '../member-tag.model';

export type PartialUpdateMemberTag = Partial<IMemberTag> & Pick<IMemberTag, 'id'>;

type RestOf<T extends IMemberTag | NewMemberTag> = Omit<T, 'createdOn' | 'updatedOn' | 'deletedOn'> & {
  createdOn?: string | null;
  updatedOn?: string | null;
  deletedOn?: string | null;
};

export type RestMemberTag = RestOf<IMemberTag>;

export type NewRestMemberTag = RestOf<NewMemberTag>;

export type PartialUpdateRestMemberTag = RestOf<PartialUpdateMemberTag>;

export type EntityResponseType = HttpResponse<IMemberTag>;
export type EntityArrayResponseType = HttpResponse<IMemberTag[]>;

@Injectable({ providedIn: 'root' })
export class MemberTagService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/member-tags');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(memberTag: NewMemberTag): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(memberTag);
    return this.http
      .post<RestMemberTag>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(memberTag: IMemberTag): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(memberTag);
    return this.http
      .put<RestMemberTag>(`${this.resourceUrl}/${this.getMemberTagIdentifier(memberTag)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(memberTag: PartialUpdateMemberTag): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(memberTag);
    return this.http
      .patch<RestMemberTag>(`${this.resourceUrl}/${this.getMemberTagIdentifier(memberTag)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestMemberTag>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestMemberTag[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getMemberTagIdentifier(memberTag: Pick<IMemberTag, 'id'>): number {
    return memberTag.id;
  }

  compareMemberTag(o1: Pick<IMemberTag, 'id'> | null, o2: Pick<IMemberTag, 'id'> | null): boolean {
    return o1 && o2 ? this.getMemberTagIdentifier(o1) === this.getMemberTagIdentifier(o2) : o1 === o2;
  }

  addMemberTagToCollectionIfMissing<Type extends Pick<IMemberTag, 'id'>>(
    memberTagCollection: Type[],
    ...memberTagsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const memberTags: Type[] = memberTagsToCheck.filter(isPresent);
    if (memberTags.length > 0) {
      const memberTagCollectionIdentifiers = memberTagCollection.map(memberTagItem => this.getMemberTagIdentifier(memberTagItem)!);
      const memberTagsToAdd = memberTags.filter(memberTagItem => {
        const memberTagIdentifier = this.getMemberTagIdentifier(memberTagItem);
        if (memberTagCollectionIdentifiers.includes(memberTagIdentifier)) {
          return false;
        }
        memberTagCollectionIdentifiers.push(memberTagIdentifier);
        return true;
      });
      return [...memberTagsToAdd, ...memberTagCollection];
    }
    return memberTagCollection;
  }

  protected convertDateFromClient<T extends IMemberTag | NewMemberTag | PartialUpdateMemberTag>(memberTag: T): RestOf<T> {
    return {
      ...memberTag,
      createdOn: memberTag.createdOn?.toJSON() ?? null,
      updatedOn: memberTag.updatedOn?.toJSON() ?? null,
      deletedOn: memberTag.deletedOn?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restMemberTag: RestMemberTag): IMemberTag {
    return {
      ...restMemberTag,
      createdOn: restMemberTag.createdOn ? dayjs(restMemberTag.createdOn) : undefined,
      updatedOn: restMemberTag.updatedOn ? dayjs(restMemberTag.updatedOn) : undefined,
      deletedOn: restMemberTag.deletedOn ? dayjs(restMemberTag.deletedOn) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestMemberTag>): HttpResponse<IMemberTag> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestMemberTag[]>): HttpResponse<IMemberTag[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
