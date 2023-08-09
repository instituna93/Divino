import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IInstrument, NewInstrument } from '../instrument.model';

export type PartialUpdateInstrument = Partial<IInstrument> & Pick<IInstrument, 'id'>;

type RestOf<T extends IInstrument | NewInstrument> = Omit<T, 'createdOn' | 'updatedOn' | 'deletedOn' | 'boughtDate'> & {
  createdOn?: string | null;
  updatedOn?: string | null;
  deletedOn?: string | null;
  boughtDate?: string | null;
};

export type RestInstrument = RestOf<IInstrument>;

export type NewRestInstrument = RestOf<NewInstrument>;

export type PartialUpdateRestInstrument = RestOf<PartialUpdateInstrument>;

export type EntityResponseType = HttpResponse<IInstrument>;
export type EntityArrayResponseType = HttpResponse<IInstrument[]>;

@Injectable({ providedIn: 'root' })
export class InstrumentService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/instruments');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(instrument: NewInstrument): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(instrument);
    return this.http
      .post<RestInstrument>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(instrument: IInstrument): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(instrument);
    return this.http
      .put<RestInstrument>(`${this.resourceUrl}/${this.getInstrumentIdentifier(instrument)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(instrument: PartialUpdateInstrument): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(instrument);
    return this.http
      .patch<RestInstrument>(`${this.resourceUrl}/${this.getInstrumentIdentifier(instrument)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestInstrument>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestInstrument[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getInstrumentIdentifier(instrument: Pick<IInstrument, 'id'>): number {
    return instrument.id;
  }

  compareInstrument(o1: Pick<IInstrument, 'id'> | null, o2: Pick<IInstrument, 'id'> | null): boolean {
    return o1 && o2 ? this.getInstrumentIdentifier(o1) === this.getInstrumentIdentifier(o2) : o1 === o2;
  }

  addInstrumentToCollectionIfMissing<Type extends Pick<IInstrument, 'id'>>(
    instrumentCollection: Type[],
    ...instrumentsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const instruments: Type[] = instrumentsToCheck.filter(isPresent);
    if (instruments.length > 0) {
      const instrumentCollectionIdentifiers = instrumentCollection.map(instrumentItem => this.getInstrumentIdentifier(instrumentItem)!);
      const instrumentsToAdd = instruments.filter(instrumentItem => {
        const instrumentIdentifier = this.getInstrumentIdentifier(instrumentItem);
        if (instrumentCollectionIdentifiers.includes(instrumentIdentifier)) {
          return false;
        }
        instrumentCollectionIdentifiers.push(instrumentIdentifier);
        return true;
      });
      return [...instrumentsToAdd, ...instrumentCollection];
    }
    return instrumentCollection;
  }

  protected convertDateFromClient<T extends IInstrument | NewInstrument | PartialUpdateInstrument>(instrument: T): RestOf<T> {
    return {
      ...instrument,
      createdOn: instrument.createdOn?.toJSON() ?? null,
      updatedOn: instrument.updatedOn?.toJSON() ?? null,
      deletedOn: instrument.deletedOn?.toJSON() ?? null,
      boughtDate: instrument.boughtDate?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restInstrument: RestInstrument): IInstrument {
    return {
      ...restInstrument,
      createdOn: restInstrument.createdOn ? dayjs(restInstrument.createdOn) : undefined,
      updatedOn: restInstrument.updatedOn ? dayjs(restInstrument.updatedOn) : undefined,
      deletedOn: restInstrument.deletedOn ? dayjs(restInstrument.deletedOn) : undefined,
      boughtDate: restInstrument.boughtDate ? dayjs(restInstrument.boughtDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestInstrument>): HttpResponse<IInstrument> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestInstrument[]>): HttpResponse<IInstrument[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
