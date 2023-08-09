import dayjs from 'dayjs/esm';
import { InstrumentType } from 'app/entities/enumerations/instrument-type.model';

export interface IInstrument {
  id: number;
  createdBy?: number | null;
  createdOn?: dayjs.Dayjs | null;
  updatedBy?: number | null;
  updatedOn?: dayjs.Dayjs | null;
  deletedBy?: number | null;
  deletedOn?: dayjs.Dayjs | null;
  name?: string | null;
  type?: keyof typeof InstrumentType | null;
  description?: string | null;
  isActive?: boolean | null;
  boughtDate?: dayjs.Dayjs | null;
  price?: number | null;
}

export type NewInstrument = Omit<IInstrument, 'id'> & { id: null };
