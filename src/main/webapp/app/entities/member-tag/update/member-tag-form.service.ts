import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IMemberTag, NewMemberTag } from '../member-tag.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IMemberTag for edit and NewMemberTagFormGroupInput for create.
 */
type MemberTagFormGroupInput = IMemberTag | PartialWithRequiredKeyOf<NewMemberTag>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IMemberTag | NewMemberTag> = Omit<T, 'createdOn' | 'updatedOn' | 'deletedOn'> & {
  createdOn?: string | null;
  updatedOn?: string | null;
  deletedOn?: string | null;
};

type MemberTagFormRawValue = FormValueOf<IMemberTag>;

type NewMemberTagFormRawValue = FormValueOf<NewMemberTag>;

type MemberTagFormDefaults = Pick<NewMemberTag, 'id' | 'createdOn' | 'updatedOn' | 'deletedOn'>;

type MemberTagFormGroupContent = {
  id: FormControl<MemberTagFormRawValue['id'] | NewMemberTag['id']>;
  createdBy: FormControl<MemberTagFormRawValue['createdBy']>;
  createdOn: FormControl<MemberTagFormRawValue['createdOn']>;
  updatedBy: FormControl<MemberTagFormRawValue['updatedBy']>;
  updatedOn: FormControl<MemberTagFormRawValue['updatedOn']>;
  deletedBy: FormControl<MemberTagFormRawValue['deletedBy']>;
  deletedOn: FormControl<MemberTagFormRawValue['deletedOn']>;
  tag: FormControl<MemberTagFormRawValue['tag']>;
  member: FormControl<MemberTagFormRawValue['member']>;
};

export type MemberTagFormGroup = FormGroup<MemberTagFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class MemberTagFormService {
  createMemberTagFormGroup(memberTag: MemberTagFormGroupInput = { id: null }): MemberTagFormGroup {
    const memberTagRawValue = this.convertMemberTagToMemberTagRawValue({
      ...this.getFormDefaults(),
      ...memberTag,
    });
    return new FormGroup<MemberTagFormGroupContent>({
      id: new FormControl(
        { value: memberTagRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      createdBy: new FormControl(memberTagRawValue.createdBy),
      createdOn: new FormControl(memberTagRawValue.createdOn),
      updatedBy: new FormControl(memberTagRawValue.updatedBy),
      updatedOn: new FormControl(memberTagRawValue.updatedOn),
      deletedBy: new FormControl(memberTagRawValue.deletedBy),
      deletedOn: new FormControl(memberTagRawValue.deletedOn),
      tag: new FormControl(memberTagRawValue.tag, {
        validators: [Validators.required],
      }),
      member: new FormControl(memberTagRawValue.member, {
        validators: [Validators.required],
      }),
    });
  }

  getMemberTag(form: MemberTagFormGroup): IMemberTag | NewMemberTag {
    return this.convertMemberTagRawValueToMemberTag(form.getRawValue() as MemberTagFormRawValue | NewMemberTagFormRawValue);
  }

  resetForm(form: MemberTagFormGroup, memberTag: MemberTagFormGroupInput): void {
    const memberTagRawValue = this.convertMemberTagToMemberTagRawValue({ ...this.getFormDefaults(), ...memberTag });
    form.reset(
      {
        ...memberTagRawValue,
        id: { value: memberTagRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): MemberTagFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdOn: currentTime,
      updatedOn: currentTime,
      deletedOn: currentTime,
    };
  }

  private convertMemberTagRawValueToMemberTag(rawMemberTag: MemberTagFormRawValue | NewMemberTagFormRawValue): IMemberTag | NewMemberTag {
    return {
      ...rawMemberTag,
      createdOn: dayjs(rawMemberTag.createdOn, DATE_TIME_FORMAT),
      updatedOn: dayjs(rawMemberTag.updatedOn, DATE_TIME_FORMAT),
      deletedOn: dayjs(rawMemberTag.deletedOn, DATE_TIME_FORMAT),
    };
  }

  private convertMemberTagToMemberTagRawValue(
    memberTag: IMemberTag | (Partial<NewMemberTag> & MemberTagFormDefaults)
  ): MemberTagFormRawValue | PartialWithRequiredKeyOf<NewMemberTagFormRawValue> {
    return {
      ...memberTag,
      createdOn: memberTag.createdOn ? memberTag.createdOn.format(DATE_TIME_FORMAT) : undefined,
      updatedOn: memberTag.updatedOn ? memberTag.updatedOn.format(DATE_TIME_FORMAT) : undefined,
      deletedOn: memberTag.deletedOn ? memberTag.deletedOn.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
