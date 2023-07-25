import dayjs from 'dayjs/esm';

import { ITagType, NewTagType } from './tag-type.model';

export const sampleWithRequiredData: ITagType = {
  id: 21715,
  description: 'Feito Nordeste Moldávia',
};

export const sampleWithPartialData: ITagType = {
  id: 11304,
  createdBy: 16608,
  createdOn: dayjs('2023-07-26T01:05'),
  updatedBy: 7214,
  deletedBy: 16594,
  deletedOn: dayjs('2023-07-26T04:23'),
  description: 'Nordeste',
};

export const sampleWithFullData: ITagType = {
  id: 8532,
  createdBy: 3604,
  createdOn: dayjs('2023-07-25T21:56'),
  updatedBy: 878,
  updatedOn: dayjs('2023-07-25T11:43'),
  deletedBy: 22222,
  deletedOn: dayjs('2023-07-25T12:55'),
  description: 'ciano',
  restricted: true,
};

export const sampleWithNewData: NewTagType = {
  description: 'Sodoeste Marinho Desporto',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
