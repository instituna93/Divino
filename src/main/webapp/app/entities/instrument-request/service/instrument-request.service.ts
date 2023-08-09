import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IInstrumentRequest, NewInstrumentRequest } from '../instrument-request.model';

export type PartialUpdateInstrumentRequest = Partial<IInstrumentRequest> & Pick<IInstrumentRequest, 'id'>;

type RestOf<T extends IInstrumentRequest | NewInstrumentRequest> = Omit<T, 'createdOn' | 'updatedOn' | 'deletedOn'> & {
  createdOn?: string | null;
  updatedOn?: string | null;
  deletedOn?: string | null;
};

export type RestInstrumentRequest = RestOf<IInstrumentRequest>;

export type NewRestInstrumentRequest = RestOf<NewInstrumentRequest>;

export type PartialUpdateRestInstrumentRequest = RestOf<PartialUpdateInstrumentRequest>;

export type EntityResponseType = HttpResponse<IInstrumentRequest>;
export type EntityArrayResponseType = HttpResponse<IInstrumentRequest[]>;

@Injectable({ providedIn: 'root' })
export class InstrumentRequestService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/instrument-requests');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(instrumentRequest: NewInstrumentRequest): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(instrumentRequest);
    return this.http
      .post<RestInstrumentRequest>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(instrumentRequest: IInstrumentRequest): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(instrumentRequest);
    return this.http
      .put<RestInstrumentRequest>(`${this.resourceUrl}/${this.getInstrumentRequestIdentifier(instrumentRequest)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(instrumentRequest: PartialUpdateInstrumentRequest): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(instrumentRequest);
    return this.http
      .patch<RestInstrumentRequest>(`${this.resourceUrl}/${this.getInstrumentRequestIdentifier(instrumentRequest)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestInstrumentRequest>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestInstrumentRequest[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getInstrumentRequestIdentifier(instrumentRequest: Pick<IInstrumentRequest, 'id'>): number {
    return instrumentRequest.id;
  }

  compareInstrumentRequest(o1: Pick<IInstrumentRequest, 'id'> | null, o2: Pick<IInstrumentRequest, 'id'> | null): boolean {
    return o1 && o2 ? this.getInstrumentRequestIdentifier(o1) === this.getInstrumentRequestIdentifier(o2) : o1 === o2;
  }

  addInstrumentRequestToCollectionIfMissing<Type extends Pick<IInstrumentRequest, 'id'>>(
    instrumentRequestCollection: Type[],
    ...instrumentRequestsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const instrumentRequests: Type[] = instrumentRequestsToCheck.filter(isPresent);
    if (instrumentRequests.length > 0) {
      const instrumentRequestCollectionIdentifiers = instrumentRequestCollection.map(
        instrumentRequestItem => this.getInstrumentRequestIdentifier(instrumentRequestItem)!
      );
      const instrumentRequestsToAdd = instrumentRequests.filter(instrumentRequestItem => {
        const instrumentRequestIdentifier = this.getInstrumentRequestIdentifier(instrumentRequestItem);
        if (instrumentRequestCollectionIdentifiers.includes(instrumentRequestIdentifier)) {
          return false;
        }
        instrumentRequestCollectionIdentifiers.push(instrumentRequestIdentifier);
        return true;
      });
      return [...instrumentRequestsToAdd, ...instrumentRequestCollection];
    }
    return instrumentRequestCollection;
  }

  protected convertDateFromClient<T extends IInstrumentRequest | NewInstrumentRequest | PartialUpdateInstrumentRequest>(
    instrumentRequest: T
  ): RestOf<T> {
    return {
      ...instrumentRequest,
      createdOn: instrumentRequest.createdOn?.toJSON() ?? null,
      updatedOn: instrumentRequest.updatedOn?.toJSON() ?? null,
      deletedOn: instrumentRequest.deletedOn?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restInstrumentRequest: RestInstrumentRequest): IInstrumentRequest {
    return {
      ...restInstrumentRequest,
      createdOn: restInstrumentRequest.createdOn ? dayjs(restInstrumentRequest.createdOn) : undefined,
      updatedOn: restInstrumentRequest.updatedOn ? dayjs(restInstrumentRequest.updatedOn) : undefined,
      deletedOn: restInstrumentRequest.deletedOn ? dayjs(restInstrumentRequest.deletedOn) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestInstrumentRequest>): HttpResponse<IInstrumentRequest> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestInstrumentRequest[]>): HttpResponse<IInstrumentRequest[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
