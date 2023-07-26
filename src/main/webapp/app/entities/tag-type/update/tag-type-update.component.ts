import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import HasAnyAuthorityDirective from 'app/shared/auth/has-any-authority.directive';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { TagTypeFormService, TagTypeFormGroup } from './tag-type-form.service';
import { ITagType } from '../tag-type.model';
import { TagTypeService } from '../service/tag-type.service';
import { ITag } from 'app/entities/tag/tag.model';
import { TagService } from 'app/entities/tag/service/tag.service';

@Component({
  standalone: true,
  selector: 'jhi-tag-type-update',
  templateUrl: './tag-type-update.component.html',
  imports: [SharedModule, HasAnyAuthorityDirective, FormsModule, ReactiveFormsModule],
})
export class TagTypeUpdateComponent implements OnInit {
  isSaving = false;
  tagType: ITagType | null = null;

  tagsSharedCollection: ITag[] = [];

  editForm: TagTypeFormGroup = this.tagTypeFormService.createTagTypeFormGroup();

  constructor(
    protected tagTypeService: TagTypeService,
    protected tagTypeFormService: TagTypeFormService,
    protected tagService: TagService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareTag = (o1: ITag | null, o2: ITag | null): boolean => this.tagService.compareTag(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tagType }) => {
      this.tagType = tagType;
      if (tagType) {
        this.updateForm(tagType);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const tagType = this.tagTypeFormService.getTagType(this.editForm);
    if (tagType.id !== null) {
      this.subscribeToSaveResponse(this.tagTypeService.update(tagType));
    } else {
      this.subscribeToSaveResponse(this.tagTypeService.create(tagType));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITagType>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(tagType: ITagType): void {
    this.tagType = tagType;
    this.tagTypeFormService.resetForm(this.editForm, tagType);

    this.tagsSharedCollection = this.tagService.addTagToCollectionIfMissing<ITag>(this.tagsSharedCollection, tagType.defaultTag);
  }

  protected loadRelationshipsOptions(): void {
    this.tagService
      .query()
      .pipe(map((res: HttpResponse<ITag[]>) => res.body ?? []))
      .pipe(map((tags: ITag[]) => this.tagService.addTagToCollectionIfMissing<ITag>(tags, this.tagType?.defaultTag)))
      .subscribe((tags: ITag[]) => (this.tagsSharedCollection = tags));
  }
}
