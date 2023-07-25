import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITagType, NewTagType } from '../tag-type.model';

export type PartialUpdateTagType = Partial<ITagType> & Pick<ITagType, 'id'>;

type RestOf<T extends ITagType | NewTagType> = Omit<T, 'createdOn' | 'updatedOn' | 'deletedOn'> & {
  createdOn?: string | null;
  updatedOn?: string | null;
  deletedOn?: string | null;
};

export type RestTagType = RestOf<ITagType>;

export type NewRestTagType = RestOf<NewTagType>;

export type PartialUpdateRestTagType = RestOf<PartialUpdateTagType>;

export type EntityResponseType = HttpResponse<ITagType>;
export type EntityArrayResponseType = HttpResponse<ITagType[]>;

@Injectable({ providedIn: 'root' })
export class TagTypeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/tag-types');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(tagType: NewTagType): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(tagType);
    return this.http
      .post<RestTagType>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(tagType: ITagType): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(tagType);
    return this.http
      .put<RestTagType>(`${this.resourceUrl}/${this.getTagTypeIdentifier(tagType)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(tagType: PartialUpdateTagType): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(tagType);
    return this.http
      .patch<RestTagType>(`${this.resourceUrl}/${this.getTagTypeIdentifier(tagType)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestTagType>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestTagType[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getTagTypeIdentifier(tagType: Pick<ITagType, 'id'>): number {
    return tagType.id;
  }

  compareTagType(o1: Pick<ITagType, 'id'> | null, o2: Pick<ITagType, 'id'> | null): boolean {
    return o1 && o2 ? this.getTagTypeIdentifier(o1) === this.getTagTypeIdentifier(o2) : o1 === o2;
  }

  addTagTypeToCollectionIfMissing<Type extends Pick<ITagType, 'id'>>(
    tagTypeCollection: Type[],
    ...tagTypesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const tagTypes: Type[] = tagTypesToCheck.filter(isPresent);
    if (tagTypes.length > 0) {
      const tagTypeCollectionIdentifiers = tagTypeCollection.map(tagTypeItem => this.getTagTypeIdentifier(tagTypeItem)!);
      const tagTypesToAdd = tagTypes.filter(tagTypeItem => {
        const tagTypeIdentifier = this.getTagTypeIdentifier(tagTypeItem);
        if (tagTypeCollectionIdentifiers.includes(tagTypeIdentifier)) {
          return false;
        }
        tagTypeCollectionIdentifiers.push(tagTypeIdentifier);
        return true;
      });
      return [...tagTypesToAdd, ...tagTypeCollection];
    }
    return tagTypeCollection;
  }

  protected convertDateFromClient<T extends ITagType | NewTagType | PartialUpdateTagType>(tagType: T): RestOf<T> {
    return {
      ...tagType,
      createdOn: tagType.createdOn?.toJSON() ?? null,
      updatedOn: tagType.updatedOn?.toJSON() ?? null,
      deletedOn: tagType.deletedOn?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restTagType: RestTagType): ITagType {
    return {
      ...restTagType,
      createdOn: restTagType.createdOn ? dayjs(restTagType.createdOn) : undefined,
      updatedOn: restTagType.updatedOn ? dayjs(restTagType.updatedOn) : undefined,
      deletedOn: restTagType.deletedOn ? dayjs(restTagType.deletedOn) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestTagType>): HttpResponse<ITagType> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestTagType[]>): HttpResponse<ITagType[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
