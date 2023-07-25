import dayjs from 'dayjs/esm';
import { ITag } from 'app/entities/tag/tag.model';
import { IMember } from 'app/entities/member/member.model';

export interface IMemberTag {
  id: number;
  createdBy?: number | null;
  createdOn?: dayjs.Dayjs | null;
  updatedBy?: number | null;
  updatedOn?: dayjs.Dayjs | null;
  deletedBy?: number | null;
  deletedOn?: dayjs.Dayjs | null;
  tag?: Pick<ITag, 'id' | 'description'> | null;
  member?: Pick<IMember, 'id' | 'nickname'> | null;
}

export type NewMemberTag = Omit<IMemberTag, 'id'> & { id: null };
