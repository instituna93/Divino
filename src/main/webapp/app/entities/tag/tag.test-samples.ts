import dayjs from 'dayjs/esm';

import { ITag, NewTag } from './tag.model';

export const sampleWithRequiredData: ITag = {
  id: 24654,
  description: 'Inteligente',
};

export const sampleWithPartialData: ITag = {
  id: 2388,
  createdBy: 28671,
  createdOn: dayjs('2023-07-25T12:55'),
  updatedBy: 16943,
  deletedBy: 1446,
  description: 'Beco bronzeado Sudeste',
};

export const sampleWithFullData: ITag = {
  id: 1903,
  createdBy: 16751,
  createdOn: dayjs('2023-07-25T20:32'),
  updatedBy: 25965,
  updatedOn: dayjs('2023-07-25T16:38'),
  deletedBy: 14579,
  deletedOn: dayjs('2023-07-25T16:25'),
  description: 'Filmes Sodoeste',
};

export const sampleWithNewData: NewTag = {
  description: 'Nordeste',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
