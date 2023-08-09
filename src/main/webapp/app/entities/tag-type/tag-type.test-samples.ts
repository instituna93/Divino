import dayjs from 'dayjs/esm';

import { ITagType, NewTagType } from './tag-type.model';

export const sampleWithRequiredData: ITagType = {
  id: 2170,
  description: 'índigo bronzeado Granada',
};

export const sampleWithPartialData: ITagType = {
  id: 8386,
  createdBy: 1716,
  createdOn: dayjs('2023-07-26T04:40'),
  updatedBy: 32723,
  updatedOn: dayjs('2023-07-25T19:24'),
  description: 'Avenida Calças',
  restricted: true,
};

export const sampleWithFullData: ITagType = {
  id: 10826,
  createdBy: 21010,
  createdOn: dayjs('2023-07-25T13:20'),
  updatedBy: 28864,
  updatedOn: dayjs('2023-07-26T03:08'),
  deletedBy: 9422,
  deletedOn: dayjs('2023-07-25T18:37'),
  description: 'Acesso Prático',
  restricted: false,
};

export const sampleWithNewData: NewTagType = {
  description: 'Paraguai Borracha',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
