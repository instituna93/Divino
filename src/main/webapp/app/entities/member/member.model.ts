import dayjs from 'dayjs/esm';
import { IUser } from 'app/entities/user/user.model';

export interface IMember {
  id: number;
  createdBy?: number | null;
  createdOn?: dayjs.Dayjs | null;
  updatedBy?: number | null;
  updatedOn?: dayjs.Dayjs | null;
  deletedBy?: number | null;
  deletedOn?: dayjs.Dayjs | null;
  nickname?: string | null;
  birthday?: dayjs.Dayjs | null;
  user?: Pick<IUser, 'id' | 'login'> | null;
}

export type NewMember = Omit<IMember, 'id'> & { id: null };
