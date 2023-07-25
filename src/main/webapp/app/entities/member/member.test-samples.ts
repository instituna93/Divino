import dayjs from 'dayjs/esm';

import { IMember, NewMember } from './member.model';

export const sampleWithRequiredData: IMember = {
  id: 17479,
};

export const sampleWithPartialData: IMember = {
  id: 7517,
  updatedBy: 23479,
  deletedBy: 27775,
  deletedOn: dayjs('2023-07-26T04:46'),
  nickname: 'Gâmbia azul',
};

export const sampleWithFullData: IMember = {
  id: 5615,
  createdBy: 15910,
  createdOn: dayjs('2023-07-25T22:43'),
  updatedBy: 22959,
  updatedOn: dayjs('2023-07-26T06:23'),
  deletedBy: 25497,
  deletedOn: dayjs('2023-07-25T23:27'),
  nickname: 'Este Pequeno',
  birthday: dayjs('2023-07-25'),
};

export const sampleWithNewData: NewMember = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
