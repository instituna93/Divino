import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IInstrument, NewInstrument } from '../instrument.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IInstrument for edit and NewInstrumentFormGroupInput for create.
 */
type InstrumentFormGroupInput = IInstrument | PartialWithRequiredKeyOf<NewInstrument>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IInstrument | NewInstrument> = Omit<T, 'createdOn' | 'updatedOn' | 'deletedOn'> & {
  createdOn?: string | null;
  updatedOn?: string | null;
  deletedOn?: string | null;
};

type InstrumentFormRawValue = FormValueOf<IInstrument>;

type NewInstrumentFormRawValue = FormValueOf<NewInstrument>;

type InstrumentFormDefaults = Pick<NewInstrument, 'id' | 'createdOn' | 'updatedOn' | 'deletedOn' | 'isActive'>;

type InstrumentFormGroupContent = {
  id: FormControl<InstrumentFormRawValue['id'] | NewInstrument['id']>;
  createdBy: FormControl<InstrumentFormRawValue['createdBy']>;
  createdOn: FormControl<InstrumentFormRawValue['createdOn']>;
  updatedBy: FormControl<InstrumentFormRawValue['updatedBy']>;
  updatedOn: FormControl<InstrumentFormRawValue['updatedOn']>;
  deletedBy: FormControl<InstrumentFormRawValue['deletedBy']>;
  deletedOn: FormControl<InstrumentFormRawValue['deletedOn']>;
  name: FormControl<InstrumentFormRawValue['name']>;
  type: FormControl<InstrumentFormRawValue['type']>;
  description: FormControl<InstrumentFormRawValue['description']>;
  isActive: FormControl<InstrumentFormRawValue['isActive']>;
  boughtDate: FormControl<InstrumentFormRawValue['boughtDate']>;
  price: FormControl<InstrumentFormRawValue['price']>;
};

export type InstrumentFormGroup = FormGroup<InstrumentFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class InstrumentFormService {
  createInstrumentFormGroup(instrument: InstrumentFormGroupInput = { id: null }): InstrumentFormGroup {
    const instrumentRawValue = this.convertInstrumentToInstrumentRawValue({
      ...this.getFormDefaults(),
      ...instrument,
    });
    return new FormGroup<InstrumentFormGroupContent>({
      id: new FormControl(
        { value: instrumentRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      createdBy: new FormControl(instrumentRawValue.createdBy),
      createdOn: new FormControl(instrumentRawValue.createdOn),
      updatedBy: new FormControl(instrumentRawValue.updatedBy),
      updatedOn: new FormControl(instrumentRawValue.updatedOn),
      deletedBy: new FormControl(instrumentRawValue.deletedBy),
      deletedOn: new FormControl(instrumentRawValue.deletedOn),
      name: new FormControl(instrumentRawValue.name, {
        validators: [Validators.required],
      }),
      type: new FormControl(instrumentRawValue.type, {
        validators: [Validators.required],
      }),
      description: new FormControl(instrumentRawValue.description),
      isActive: new FormControl(instrumentRawValue.isActive, {
        validators: [Validators.required],
      }),
      boughtDate: new FormControl(instrumentRawValue.boughtDate),
      price: new FormControl(instrumentRawValue.price, {
        validators: [Validators.min(0)],
      }),
    });
  }

  getInstrument(form: InstrumentFormGroup): IInstrument | NewInstrument {
    return this.convertInstrumentRawValueToInstrument(form.getRawValue() as InstrumentFormRawValue | NewInstrumentFormRawValue);
  }

  resetForm(form: InstrumentFormGroup, instrument: InstrumentFormGroupInput): void {
    const instrumentRawValue = this.convertInstrumentToInstrumentRawValue({ ...this.getFormDefaults(), ...instrument });
    form.reset(
      {
        ...instrumentRawValue,
        id: { value: instrumentRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): InstrumentFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdOn: currentTime,
      updatedOn: currentTime,
      deletedOn: currentTime,
      isActive: false,
    };
  }

  private convertInstrumentRawValueToInstrument(
    rawInstrument: InstrumentFormRawValue | NewInstrumentFormRawValue
  ): IInstrument | NewInstrument {
    return {
      ...rawInstrument,
      createdOn: dayjs(rawInstrument.createdOn, DATE_TIME_FORMAT),
      updatedOn: dayjs(rawInstrument.updatedOn, DATE_TIME_FORMAT),
      deletedOn: dayjs(rawInstrument.deletedOn, DATE_TIME_FORMAT),
    };
  }

  private convertInstrumentToInstrumentRawValue(
    instrument: IInstrument | (Partial<NewInstrument> & InstrumentFormDefaults)
  ): InstrumentFormRawValue | PartialWithRequiredKeyOf<NewInstrumentFormRawValue> {
    return {
      ...instrument,
      createdOn: instrument.createdOn ? instrument.createdOn.format(DATE_TIME_FORMAT) : undefined,
      updatedOn: instrument.updatedOn ? instrument.updatedOn.format(DATE_TIME_FORMAT) : undefined,
      deletedOn: instrument.deletedOn ? instrument.deletedOn.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
