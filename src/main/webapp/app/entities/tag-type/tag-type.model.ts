import dayjs from 'dayjs/esm';
import { ITag } from 'app/entities/tag/tag.model';

export interface ITagType {
  id: number;
  createdBy?: number | null;
  createdOn?: dayjs.Dayjs | null;
  updatedBy?: number | null;
  updatedOn?: dayjs.Dayjs | null;
  deletedBy?: number | null;
  deletedOn?: dayjs.Dayjs | null;
  description?: string | null;
  restricted?: boolean | null;
  defaultTag?: Pick<ITag, 'id' | 'description'> | null;
}

export type NewTagType = Omit<ITagType, 'id'> & { id: null };
