import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IMember, NewMember } from '../member.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IMember for edit and NewMemberFormGroupInput for create.
 */
type MemberFormGroupInput = IMember | PartialWithRequiredKeyOf<NewMember>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IMember | NewMember> = Omit<T, 'createdOn' | 'updatedOn' | 'deletedOn'> & {
  createdOn?: string | null;
  updatedOn?: string | null;
  deletedOn?: string | null;
};

type MemberFormRawValue = FormValueOf<IMember>;

type NewMemberFormRawValue = FormValueOf<NewMember>;

type MemberFormDefaults = Pick<NewMember, 'id' | 'createdOn' | 'updatedOn' | 'deletedOn'>;

type MemberFormGroupContent = {
  id: FormControl<MemberFormRawValue['id'] | NewMember['id']>;
  createdBy: FormControl<MemberFormRawValue['createdBy']>;
  createdOn: FormControl<MemberFormRawValue['createdOn']>;
  updatedBy: FormControl<MemberFormRawValue['updatedBy']>;
  updatedOn: FormControl<MemberFormRawValue['updatedOn']>;
  deletedBy: FormControl<MemberFormRawValue['deletedBy']>;
  deletedOn: FormControl<MemberFormRawValue['deletedOn']>;
  nickname: FormControl<MemberFormRawValue['nickname']>;
  birthday: FormControl<MemberFormRawValue['birthday']>;
  user: FormControl<MemberFormRawValue['user']>;
};

export type MemberFormGroup = FormGroup<MemberFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class MemberFormService {
  createMemberFormGroup(member: MemberFormGroupInput = { id: null }): MemberFormGroup {
    const memberRawValue = this.convertMemberToMemberRawValue({
      ...this.getFormDefaults(),
      ...member,
    });
    return new FormGroup<MemberFormGroupContent>({
      id: new FormControl(
        { value: memberRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      createdBy: new FormControl({ value: memberRawValue.createdBy, disabled: true }),
      createdOn: new FormControl({ value: memberRawValue.createdOn, disabled: true }),
      updatedBy: new FormControl({ value: memberRawValue.updatedBy, disabled: true }),
      updatedOn: new FormControl({ value: memberRawValue.updatedOn, disabled: true }),
      deletedBy: new FormControl({ value: memberRawValue.deletedBy, disabled: true }),
      deletedOn: new FormControl({ value: memberRawValue.deletedOn, disabled: true }),
      nickname: new FormControl(memberRawValue.nickname),
      birthday: new FormControl(memberRawValue.birthday),
      user: new FormControl(memberRawValue.user),
    });
  }

  getMember(form: MemberFormGroup): IMember | NewMember {
    return this.convertMemberRawValueToMember(form.getRawValue() as MemberFormRawValue | NewMemberFormRawValue);
  }

  resetForm(form: MemberFormGroup, member: MemberFormGroupInput): void {
    const memberRawValue = this.convertMemberToMemberRawValue({ ...this.getFormDefaults(), ...member });
    form.reset(
      {
        ...memberRawValue,
        id: { value: memberRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): MemberFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdOn: currentTime,
      updatedOn: currentTime,
      deletedOn: currentTime,
    };
  }

  private convertMemberRawValueToMember(rawMember: MemberFormRawValue | NewMemberFormRawValue): IMember | NewMember {
    return {
      ...rawMember,
      createdOn: dayjs(rawMember.createdOn, DATE_TIME_FORMAT),
      updatedOn: dayjs(rawMember.updatedOn, DATE_TIME_FORMAT),
      deletedOn: dayjs(rawMember.deletedOn, DATE_TIME_FORMAT),
    };
  }

  private convertMemberToMemberRawValue(
    member: IMember | (Partial<NewMember> & MemberFormDefaults)
  ): MemberFormRawValue | PartialWithRequiredKeyOf<NewMemberFormRawValue> {
    return {
      ...member,
      createdOn: member.createdOn ? member.createdOn.format(DATE_TIME_FORMAT) : undefined,
      updatedOn: member.updatedOn ? member.updatedOn.format(DATE_TIME_FORMAT) : undefined,
      deletedOn: member.deletedOn ? member.deletedOn.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
