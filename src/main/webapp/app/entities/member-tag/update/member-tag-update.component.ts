import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import HasAnyAuthorityDirective from 'app/shared/auth/has-any-authority.directive';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { MemberTagFormService, MemberTagFormGroup } from './member-tag-form.service';
import { IMemberTag } from '../member-tag.model';
import { MemberTagService } from '../service/member-tag.service';
import { ITag } from 'app/entities/tag/tag.model';
import { TagService } from 'app/entities/tag/service/tag.service';
import { IMember } from 'app/entities/member/member.model';
import { MemberService } from 'app/entities/member/service/member.service';

@Component({
  standalone: true,
  selector: 'jhi-member-tag-update',
  templateUrl: './member-tag-update.component.html',
  imports: [SharedModule, HasAnyAuthorityDirective, FormsModule, ReactiveFormsModule],
})
export class MemberTagUpdateComponent implements OnInit {
  isSaving = false;
  memberTag: IMemberTag | null = null;

  tagsSharedCollection: ITag[] = [];
  membersSharedCollection: IMember[] = [];

  editForm: MemberTagFormGroup = this.memberTagFormService.createMemberTagFormGroup();

  constructor(
    protected memberTagService: MemberTagService,
    protected memberTagFormService: MemberTagFormService,
    protected tagService: TagService,
    protected memberService: MemberService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareTag = (o1: ITag | null, o2: ITag | null): boolean => this.tagService.compareTag(o1, o2);

  compareMember = (o1: IMember | null, o2: IMember | null): boolean => this.memberService.compareMember(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ memberTag }) => {
      this.memberTag = memberTag;
      if (memberTag) {
        this.updateForm(memberTag);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const memberTag = this.memberTagFormService.getMemberTag(this.editForm);
    if (memberTag.id !== null) {
      this.subscribeToSaveResponse(this.memberTagService.update(memberTag));
    } else {
      this.subscribeToSaveResponse(this.memberTagService.create(memberTag));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMemberTag>>): void {
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

  protected updateForm(memberTag: IMemberTag): void {
    this.memberTag = memberTag;
    this.memberTagFormService.resetForm(this.editForm, memberTag);

    this.tagsSharedCollection = this.tagService.addTagToCollectionIfMissing<ITag>(this.tagsSharedCollection, memberTag.tag);
    this.membersSharedCollection = this.memberService.addMemberToCollectionIfMissing<IMember>(
      this.membersSharedCollection,
      memberTag.member
    );
  }

  protected loadRelationshipsOptions(): void {
    this.tagService
      .query()
      .pipe(map((res: HttpResponse<ITag[]>) => res.body ?? []))
      .pipe(map((tags: ITag[]) => this.tagService.addTagToCollectionIfMissing<ITag>(tags, this.memberTag?.tag)))
      .subscribe((tags: ITag[]) => (this.tagsSharedCollection = tags));

    this.memberService
      .query()
      .pipe(map((res: HttpResponse<IMember[]>) => res.body ?? []))
      .pipe(map((members: IMember[]) => this.memberService.addMemberToCollectionIfMissing<IMember>(members, this.memberTag?.member)))
      .subscribe((members: IMember[]) => (this.membersSharedCollection = members));
  }
}
