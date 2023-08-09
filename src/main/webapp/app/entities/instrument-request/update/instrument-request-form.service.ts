import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IInstrumentRequest, NewInstrumentRequest } from '../instrument-request.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IInstrumentRequest for edit and NewInstrumentRequestFormGroupInput for create.
 */
type InstrumentRequestFormGroupInput = IInstrumentRequest | PartialWithRequiredKeyOf<NewInstrumentRequest>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IInstrumentRequest | NewInstrumentRequest> = Omit<T, 'createdOn' | 'updatedOn' | 'deletedOn'> & {
  createdOn?: string | null;
  updatedOn?: string | null;
  deletedOn?: string | null;
};

type InstrumentRequestFormRawValue = FormValueOf<IInstrumentRequest>;

type NewInstrumentRequestFormRawValue = FormValueOf<NewInstrumentRequest>;

type InstrumentRequestFormDefaults = Pick<NewInstrumentRequest, 'id' | 'createdOn' | 'updatedOn' | 'deletedOn' | 'isReturned'>;

type InstrumentRequestFormGroupContent = {
  id: FormControl<InstrumentRequestFormRawValue['id'] | NewInstrumentRequest['id']>;
  createdBy: FormControl<InstrumentRequestFormRawValue['createdBy']>;
  createdOn: FormControl<InstrumentRequestFormRawValue['createdOn']>;
  updatedBy: FormControl<InstrumentRequestFormRawValue['updatedBy']>;
  updatedOn: FormControl<InstrumentRequestFormRawValue['updatedOn']>;
  deletedBy: FormControl<InstrumentRequestFormRawValue['deletedBy']>;
  deletedOn: FormControl<InstrumentRequestFormRawValue['deletedOn']>;
  description: FormControl<InstrumentRequestFormRawValue['description']>;
  isReturned: FormControl<InstrumentRequestFormRawValue['isReturned']>;
  instrument: FormControl<InstrumentRequestFormRawValue['instrument']>;
  member: FormControl<InstrumentRequestFormRawValue['member']>;
};

export type InstrumentRequestFormGroup = FormGroup<InstrumentRequestFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class InstrumentRequestFormService {
  createInstrumentRequestFormGroup(instrumentRequest: InstrumentRequestFormGroupInput = { id: null }): InstrumentRequestFormGroup {
    const instrumentRequestRawValue = this.convertInstrumentRequestToInstrumentRequestRawValue({
      ...this.getFormDefaults(),
      ...instrumentRequest,
    });
    return new FormGroup<InstrumentRequestFormGroupContent>({
      id: new FormControl(
        { value: instrumentRequestRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      createdBy: new FormControl(instrumentRequestRawValue.createdBy),
      createdOn: new FormControl(instrumentRequestRawValue.createdOn),
      updatedBy: new FormControl(instrumentRequestRawValue.updatedBy),
      updatedOn: new FormControl(instrumentRequestRawValue.updatedOn),
      deletedBy: new FormControl(instrumentRequestRawValue.deletedBy),
      deletedOn: new FormControl(instrumentRequestRawValue.deletedOn),
      description: new FormControl(instrumentRequestRawValue.description),
      isReturned: new FormControl(instrumentRequestRawValue.isReturned),
      instrument: new FormControl(instrumentRequestRawValue.instrument, {
        validators: [Validators.required],
      }),
      member: new FormControl(instrumentRequestRawValue.member, {
        validators: [Validators.required],
      }),
    });
  }

  getInstrumentRequest(form: InstrumentRequestFormGroup): IInstrumentRequest | NewInstrumentRequest {
    return this.convertInstrumentRequestRawValueToInstrumentRequest(
      form.getRawValue() as InstrumentRequestFormRawValue | NewInstrumentRequestFormRawValue
    );
  }

  resetForm(form: InstrumentRequestFormGroup, instrumentRequest: InstrumentRequestFormGroupInput): void {
    const instrumentRequestRawValue = this.convertInstrumentRequestToInstrumentRequestRawValue({
      ...this.getFormDefaults(),
      ...instrumentRequest,
    });
    form.reset(
      {
        ...instrumentRequestRawValue,
        id: { value: instrumentRequestRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): InstrumentRequestFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdOn: currentTime,
      updatedOn: currentTime,
      deletedOn: currentTime,
      isReturned: false,
    };
  }

  private convertInstrumentRequestRawValueToInstrumentRequest(
    rawInstrumentRequest: InstrumentRequestFormRawValue | NewInstrumentRequestFormRawValue
  ): IInstrumentRequest | NewInstrumentRequest {
    return {
      ...rawInstrumentRequest,
      createdOn: dayjs(rawInstrumentRequest.createdOn, DATE_TIME_FORMAT),
      updatedOn: dayjs(rawInstrumentRequest.updatedOn, DATE_TIME_FORMAT),
      deletedOn: dayjs(rawInstrumentRequest.deletedOn, DATE_TIME_FORMAT),
    };
  }

  private convertInstrumentRequestToInstrumentRequestRawValue(
    instrumentRequest: IInstrumentRequest | (Partial<NewInstrumentRequest> & InstrumentRequestFormDefaults)
  ): InstrumentRequestFormRawValue | PartialWithRequiredKeyOf<NewInstrumentRequestFormRawValue> {
    return {
      ...instrumentRequest,
      createdOn: instrumentRequest.createdOn ? instrumentRequest.createdOn.format(DATE_TIME_FORMAT) : undefined,
      updatedOn: instrumentRequest.updatedOn ? instrumentRequest.updatedOn.format(DATE_TIME_FORMAT) : undefined,
      deletedOn: instrumentRequest.deletedOn ? instrumentRequest.deletedOn.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
