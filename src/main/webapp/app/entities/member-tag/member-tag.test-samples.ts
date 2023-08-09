import dayjs from 'dayjs/esm';

import { IMemberTag, NewMemberTag } from './member-tag.model';

export const sampleWithRequiredData: IMemberTag = {
  id: 8872,
};

export const sampleWithPartialData: IMemberTag = {
  id: 9187,
  createdBy: 29127,
  createdOn: dayjs('2023-07-25T13:46'),
  updatedBy: 19012,
  updatedOn: dayjs('2023-07-25T23:44'),
  deletedBy: 10021,
};

export const sampleWithFullData: IMemberTag = {
  id: 31678,
  createdBy: 31367,
  createdOn: dayjs('2023-07-25T18:58'),
  updatedBy: 13770,
  updatedOn: dayjs('2023-07-25T22:33'),
  deletedBy: 13363,
  deletedOn: dayjs('2023-07-25T08:40'),
};

export const sampleWithNewData: NewMemberTag = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
