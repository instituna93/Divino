import dayjs from 'dayjs/esm';

import { InstrumentType } from 'app/entities/enumerations/instrument-type.model';

import { IInstrument, NewInstrument } from './instrument.model';

export const sampleWithRequiredData: IInstrument = {
  id: 4535,
  name: 'Bebé Níger',
  type: 'Outros',
  isActive: false,
};

export const sampleWithPartialData: IInstrument = {
  id: 4589,
  updatedBy: 27049,
  updatedOn: dayjs('2023-08-08T01:49'),
  deletedOn: dayjs('2023-08-08T04:30'),
  name: 'Jordânia',
  type: 'Cordas',
  description: 'Betão',
  isActive: false,
  boughtDate: dayjs('2023-08-08'),
};

export const sampleWithFullData: IInstrument = {
  id: 17589,
  createdBy: 9415,
  createdOn: dayjs('2023-08-08T16:41'),
  updatedBy: 16246,
  updatedOn: dayjs('2023-08-08T11:14'),
  deletedBy: 18804,
  deletedOn: dayjs('2023-08-08T13:56'),
  name: 'Borracha',
  type: 'Outros',
  description: 'Pequeno',
  isActive: true,
  boughtDate: dayjs('2023-08-08'),
  price: 8345,
};

export const sampleWithNewData: NewInstrument = {
  name: 'Aço índigo',
  type: 'Outros',
  isActive: true,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
