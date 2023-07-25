import dayjs from 'dayjs/esm';
import { ITagType } from 'app/entities/tag-type/tag-type.model';

export interface ITag {
  id: number;
  createdBy?: number | null;
  createdOn?: dayjs.Dayjs | null;
  updatedBy?: number | null;
  updatedOn?: dayjs.Dayjs | null;
  deletedBy?: number | null;
  deletedOn?: dayjs.Dayjs | null;
  description?: string | null;
  tagType?: Pick<ITagType, 'id' | 'description'> | null;
}

export type NewTag = Omit<ITag, 'id'> & { id: null };
