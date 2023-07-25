import dayjs from 'dayjs/esm';

import { IMemberTag, NewMemberTag } from './member-tag.model';

export const sampleWithRequiredData: IMemberTag = {
  id: 7791,
};

export const sampleWithPartialData: IMemberTag = {
  id: 21368,
  createdOn: dayjs('2023-07-25T12:14'),
  updatedBy: 21308,
  updatedOn: dayjs('2023-07-25T14:31'),
  deletedBy: 29269,
  deletedOn: dayjs('2023-07-25T09:14'),
};

export const sampleWithFullData: IMemberTag = {
  id: 3766,
  createdBy: 10668,
  createdOn: dayjs('2023-07-25T12:59'),
  updatedBy: 11025,
  updatedOn: dayjs('2023-07-25T17:27'),
  deletedBy: 18729,
  deletedOn: dayjs('2023-07-25T14:05'),
};

export const sampleWithNewData: NewMemberTag = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
