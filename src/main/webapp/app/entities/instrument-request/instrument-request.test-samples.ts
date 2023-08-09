import dayjs from 'dayjs/esm';

import { IInstrumentRequest, NewInstrumentRequest } from './instrument-request.model';

export const sampleWithRequiredData: IInstrumentRequest = {
  id: 1225,
};

export const sampleWithPartialData: IInstrumentRequest = {
  id: 11801,
  createdOn: dayjs('2023-08-08T14:49'),
  updatedBy: 4799,
  updatedOn: dayjs('2023-08-08T01:33'),
  deletedOn: dayjs('2023-08-08T02:10'),
  isReturned: false,
};

export const sampleWithFullData: IInstrumentRequest = {
  id: 9708,
  createdBy: 27708,
  createdOn: dayjs('2023-08-08T02:15'),
  updatedBy: 16052,
  updatedOn: dayjs('2023-08-08T18:19'),
  deletedBy: 4168,
  deletedOn: dayjs('2023-08-08T11:58'),
  description: 'turquesa Borracha',
  isReturned: true,
};

export const sampleWithNewData: NewInstrumentRequest = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
