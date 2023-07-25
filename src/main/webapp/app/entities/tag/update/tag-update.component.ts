import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { TagFormService, TagFormGroup } from './tag-form.service';
import { ITag } from '../tag.model';
import { TagService } from '../service/tag.service';
import { ITagType } from 'app/entities/tag-type/tag-type.model';
import { TagTypeService } from 'app/entities/tag-type/service/tag-type.service';

@Component({
  standalone: true,
  selector: 'jhi-tag-update',
  templateUrl: './tag-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class TagUpdateComponent implements OnInit {
  isSaving = false;
  tag: ITag | null = null;

  tagTypesSharedCollection: ITagType[] = [];

  editForm: TagFormGroup = this.tagFormService.createTagFormGroup();

  constructor(
    protected tagService: TagService,
    protected tagFormService: TagFormService,
    protected tagTypeService: TagTypeService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareTagType = (o1: ITagType | null, o2: ITagType | null): boolean => this.tagTypeService.compareTagType(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tag }) => {
      this.tag = tag;
      if (tag) {
        this.updateForm(tag);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const tag = this.tagFormService.getTag(this.editForm);
    if (tag.id !== null) {
      this.subscribeToSaveResponse(this.tagService.update(tag));
    } else {
      this.subscribeToSaveResponse(this.tagService.create(tag));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITag>>): void {
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

  protected updateForm(tag: ITag): void {
    this.tag = tag;
    this.tagFormService.resetForm(this.editForm, tag);

    this.tagTypesSharedCollection = this.tagTypeService.addTagTypeToCollectionIfMissing<ITagType>(
      this.tagTypesSharedCollection,
      tag.tagType
    );
  }

  protected loadRelationshipsOptions(): void {
    this.tagTypeService
      .query()
      .pipe(map((res: HttpResponse<ITagType[]>) => res.body ?? []))
      .pipe(map((tagTypes: ITagType[]) => this.tagTypeService.addTagTypeToCollectionIfMissing<ITagType>(tagTypes, this.tag?.tagType)))
      .subscribe((tagTypes: ITagType[]) => (this.tagTypesSharedCollection = tagTypes));
  }
}
