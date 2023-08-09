import dayjs from 'dayjs/esm';

import { ITag, NewTag } from './tag.model';

export const sampleWithRequiredData: ITag = {
  id: 4507,
  description: 'Sodoeste Incrível Inteligente',
};

export const sampleWithPartialData: ITag = {
  id: 11633,
  createdBy: 13945,
  createdOn: dayjs('2023-07-26T01:15'),
  updatedOn: dayjs('2023-07-25T22:47'),
  deletedOn: dayjs('2023-07-25T12:21'),
  description: 'Sul Norte',
};

export const sampleWithFullData: ITag = {
  id: 25042,
  createdBy: 6234,
  createdOn: dayjs('2023-07-25T16:59'),
  updatedBy: 24450,
  updatedOn: dayjs('2023-07-26T02:32'),
  deletedBy: 5160,
  deletedOn: dayjs('2023-07-25T20:45'),
  description: 'Tomé Fantástico Noroeste',
};

export const sampleWithNewData: NewTag = {
  description: 'Sodoeste Tailândia Nordeste',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
