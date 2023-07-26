import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ITag, NewTag } from '../tag.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITag for edit and NewTagFormGroupInput for create.
 */
type TagFormGroupInput = ITag | PartialWithRequiredKeyOf<NewTag>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ITag | NewTag> = Omit<T, 'createdOn' | 'updatedOn' | 'deletedOn'> & {
  createdOn?: string | null;
  updatedOn?: string | null;
  deletedOn?: string | null;
};

type TagFormRawValue = FormValueOf<ITag>;

type NewTagFormRawValue = FormValueOf<NewTag>;

type TagFormDefaults = Pick<NewTag, 'id' | 'createdOn' | 'updatedOn' | 'deletedOn'>;

type TagFormGroupContent = {
  id: FormControl<TagFormRawValue['id'] | NewTag['id']>;
  createdBy: FormControl<TagFormRawValue['createdBy']>;
  createdOn: FormControl<TagFormRawValue['createdOn']>;
  updatedBy: FormControl<TagFormRawValue['updatedBy']>;
  updatedOn: FormControl<TagFormRawValue['updatedOn']>;
  deletedBy: FormControl<TagFormRawValue['deletedBy']>;
  deletedOn: FormControl<TagFormRawValue['deletedOn']>;
  description: FormControl<TagFormRawValue['description']>;
  tagType: FormControl<TagFormRawValue['tagType']>;
};

export type TagFormGroup = FormGroup<TagFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TagFormService {
  createTagFormGroup(tag: TagFormGroupInput = { id: null }): TagFormGroup {
    const tagRawValue = this.convertTagToTagRawValue({
      ...this.getFormDefaults(),
      ...tag,
    });
    return new FormGroup<TagFormGroupContent>({
      id: new FormControl(
        { value: tagRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      createdBy: new FormControl({ value: tagRawValue.createdBy, disabled: true }),
      createdOn: new FormControl({ value: tagRawValue.createdOn, disabled: true }),
      updatedBy: new FormControl({ value: tagRawValue.updatedBy, disabled: true }),
      updatedOn: new FormControl({ value: tagRawValue.updatedOn, disabled: true }),
      deletedBy: new FormControl({ value: tagRawValue.deletedBy, disabled: true }),
      deletedOn: new FormControl({ value: tagRawValue.deletedOn, disabled: true }),
      description: new FormControl(tagRawValue.description, {
        validators: [Validators.required],
      }),
      tagType: new FormControl(tagRawValue.tagType, {
        validators: [Validators.required],
      }),
    });
  }

  getTag(form: TagFormGroup): ITag | NewTag {
    return this.convertTagRawValueToTag(form.getRawValue() as TagFormRawValue | NewTagFormRawValue);
  }

  resetForm(form: TagFormGroup, tag: TagFormGroupInput): void {
    const tagRawValue = this.convertTagToTagRawValue({ ...this.getFormDefaults(), ...tag });
    form.reset(
      {
        ...tagRawValue,
        id: { value: tagRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): TagFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdOn: currentTime,
      updatedOn: currentTime,
      deletedOn: currentTime,
    };
  }

  private convertTagRawValueToTag(rawTag: TagFormRawValue | NewTagFormRawValue): ITag | NewTag {
    return {
      ...rawTag,
      createdOn: dayjs(rawTag.createdOn, DATE_TIME_FORMAT),
      updatedOn: dayjs(rawTag.updatedOn, DATE_TIME_FORMAT),
      deletedOn: dayjs(rawTag.deletedOn, DATE_TIME_FORMAT),
    };
  }

  private convertTagToTagRawValue(
    tag: ITag | (Partial<NewTag> & TagFormDefaults)
  ): TagFormRawValue | PartialWithRequiredKeyOf<NewTagFormRawValue> {
    return {
      ...tag,
      createdOn: tag.createdOn ? tag.createdOn.format(DATE_TIME_FORMAT) : undefined,
      updatedOn: tag.updatedOn ? tag.updatedOn.format(DATE_TIME_FORMAT) : undefined,
      deletedOn: tag.deletedOn ? tag.deletedOn.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
