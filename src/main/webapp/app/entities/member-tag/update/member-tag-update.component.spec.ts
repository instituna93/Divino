import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { MemberTagFormService } from './member-tag-form.service';
import { MemberTagService } from '../service/member-tag.service';
import { IMemberTag } from '../member-tag.model';
import { ITag } from 'app/entities/tag/tag.model';
import { TagService } from 'app/entities/tag/service/tag.service';
import { IMember } from 'app/entities/member/member.model';
import { MemberService } from 'app/entities/member/service/member.service';

import { MemberTagUpdateComponent } from './member-tag-update.component';

describe('MemberTag Management Update Component', () => {
  let comp: MemberTagUpdateComponent;
  let fixture: ComponentFixture<MemberTagUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let memberTagFormService: MemberTagFormService;
  let memberTagService: MemberTagService;
  let tagService: TagService;
  let memberService: MemberService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), MemberTagUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(MemberTagUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MemberTagUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    memberTagFormService = TestBed.inject(MemberTagFormService);
    memberTagService = TestBed.inject(MemberTagService);
    tagService = TestBed.inject(TagService);
    memberService = TestBed.inject(MemberService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Tag query and add missing value', () => {
      const memberTag: IMemberTag = { id: 456 };
      const tag: ITag = { id: 9363 };
      memberTag.tag = tag;

      const tagCollection: ITag[] = [{ id: 16224 }];
      jest.spyOn(tagService, 'query').mockReturnValue(of(new HttpResponse({ body: tagCollection })));
      const additionalTags = [tag];
      const expectedCollection: ITag[] = [...additionalTags, ...tagCollection];
      jest.spyOn(tagService, 'addTagToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ memberTag });
      comp.ngOnInit();

      expect(tagService.query).toHaveBeenCalled();
      expect(tagService.addTagToCollectionIfMissing).toHaveBeenCalledWith(tagCollection, ...additionalTags.map(expect.objectContaining));
      expect(comp.tagsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Member query and add missing value', () => {
      const memberTag: IMemberTag = { id: 456 };
      const member: IMember = { id: 2431 };
      memberTag.member = member;

      const memberCollection: IMember[] = [{ id: 3304 }];
      jest.spyOn(memberService, 'query').mockReturnValue(of(new HttpResponse({ body: memberCollection })));
      const additionalMembers = [member];
      const expectedCollection: IMember[] = [...additionalMembers, ...memberCollection];
      jest.spyOn(memberService, 'addMemberToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ memberTag });
      comp.ngOnInit();

      expect(memberService.query).toHaveBeenCalled();
      expect(memberService.addMemberToCollectionIfMissing).toHaveBeenCalledWith(
        memberCollection,
        ...additionalMembers.map(expect.objectContaining)
      );
      expect(comp.membersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const memberTag: IMemberTag = { id: 456 };
      const tag: ITag = { id: 7508 };
      memberTag.tag = tag;
      const member: IMember = { id: 10726 };
      memberTag.member = member;

      activatedRoute.data = of({ memberTag });
      comp.ngOnInit();

      expect(comp.tagsSharedCollection).toContain(tag);
      expect(comp.membersSharedCollection).toContain(member);
      expect(comp.memberTag).toEqual(memberTag);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMemberTag>>();
      const memberTag = { id: 123 };
      jest.spyOn(memberTagFormService, 'getMemberTag').mockReturnValue(memberTag);
      jest.spyOn(memberTagService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ memberTag });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: memberTag }));
      saveSubject.complete();

      // THEN
      expect(memberTagFormService.getMemberTag).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(memberTagService.update).toHaveBeenCalledWith(expect.objectContaining(memberTag));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMemberTag>>();
      const memberTag = { id: 123 };
      jest.spyOn(memberTagFormService, 'getMemberTag').mockReturnValue({ id: null });
      jest.spyOn(memberTagService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ memberTag: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: memberTag }));
      saveSubject.complete();

      // THEN
      expect(memberTagFormService.getMemberTag).toHaveBeenCalled();
      expect(memberTagService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMemberTag>>();
      const memberTag = { id: 123 };
      jest.spyOn(memberTagService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ memberTag });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(memberTagService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareTag', () => {
      it('Should forward to tagService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(tagService, 'compareTag');
        comp.compareTag(entity, entity2);
        expect(tagService.compareTag).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareMember', () => {
      it('Should forward to memberService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(memberService, 'compareMember');
        comp.compareMember(entity, entity2);
        expect(memberService.compareMember).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
