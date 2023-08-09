import dayjs from 'dayjs/esm';
import { IInstrument } from 'app/entities/instrument/instrument.model';
import { IMember } from 'app/entities/member/member.model';

export interface IInstrumentRequest {
  id: number;
  createdBy?: number | null;
  createdOn?: dayjs.Dayjs | null;
  updatedBy?: number | null;
  updatedOn?: dayjs.Dayjs | null;
  deletedBy?: number | null;
  deletedOn?: dayjs.Dayjs | null;
  description?: string | null;
  isReturned?: boolean | null;
  instrument?: Pick<IInstrument, 'id' | 'name'> | null;
  member?: Pick<IMember, 'id' | 'nickname'> | null;
}

export type NewInstrumentRequest = Omit<IInstrumentRequest, 'id'> & { id: null };
