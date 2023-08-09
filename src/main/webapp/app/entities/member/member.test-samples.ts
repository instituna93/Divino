import dayjs from 'dayjs/esm';

import { IMember, NewMember } from './member.model';

export const sampleWithRequiredData: IMember = {
  id: 3437,
};

export const sampleWithPartialData: IMember = {
  id: 12596,
  createdOn: dayjs('2023-07-26T04:14'),
  updatedBy: 15075,
  deletedBy: 20025,
  deletedOn: dayjs('2023-07-26T06:06'),
};

export const sampleWithFullData: IMember = {
  id: 27282,
  createdBy: 25663,
  createdOn: dayjs('2023-07-25T13:48'),
  updatedBy: 22228,
  updatedOn: dayjs('2023-07-25T18:02'),
  deletedBy: 2338,
  deletedOn: dayjs('2023-07-25T14:47'),
  nickname: 'Sul',
  birthday: dayjs('2023-07-26'),
};

export const sampleWithNewData: NewMember = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
