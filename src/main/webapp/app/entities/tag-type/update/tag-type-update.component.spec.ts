import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { TagTypeFormService } from './tag-type-form.service';
import { TagTypeService } from '../service/tag-type.service';
import { ITagType } from '../tag-type.model';
import { ITag } from 'app/entities/tag/tag.model';
import { TagService } from 'app/entities/tag/service/tag.service';

import { TagTypeUpdateComponent } from './tag-type-update.component';

describe('TagType Management Update Component', () => {
  let comp: TagTypeUpdateComponent;
  let fixture: ComponentFixture<TagTypeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let tagTypeFormService: TagTypeFormService;
  let tagTypeService: TagTypeService;
  let tagService: TagService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), TagTypeUpdateComponent],
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
      .overrideTemplate(TagTypeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TagTypeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    tagTypeFormService = TestBed.inject(TagTypeFormService);
    tagTypeService = TestBed.inject(TagTypeService);
    tagService = TestBed.inject(TagService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Tag query and add missing value', () => {
      const tagType: ITagType = { id: 456 };
      const defaultTag: ITag = { id: 16588 };
      tagType.defaultTag = defaultTag;

      const tagCollection: ITag[] = [{ id: 22414 }];
      jest.spyOn(tagService, 'query').mockReturnValue(of(new HttpResponse({ body: tagCollection })));
      const additionalTags = [defaultTag];
      const expectedCollection: ITag[] = [...additionalTags, ...tagCollection];
      jest.spyOn(tagService, 'addTagToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ tagType });
      comp.ngOnInit();

      expect(tagService.query).toHaveBeenCalled();
      expect(tagService.addTagToCollectionIfMissing).toHaveBeenCalledWith(tagCollection, ...additionalTags.map(expect.objectContaining));
      expect(comp.tagsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const tagType: ITagType = { id: 456 };
      const defaultTag: ITag = { id: 3682 };
      tagType.defaultTag = defaultTag;

      activatedRoute.data = of({ tagType });
      comp.ngOnInit();

      expect(comp.tagsSharedCollection).toContain(defaultTag);
      expect(comp.tagType).toEqual(tagType);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITagType>>();
      const tagType = { id: 123 };
      jest.spyOn(tagTypeFormService, 'getTagType').mockReturnValue(tagType);
      jest.spyOn(tagTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tagType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tagType }));
      saveSubject.complete();

      // THEN
      expect(tagTypeFormService.getTagType).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(tagTypeService.update).toHaveBeenCalledWith(expect.objectContaining(tagType));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITagType>>();
      const tagType = { id: 123 };
      jest.spyOn(tagTypeFormService, 'getTagType').mockReturnValue({ id: null });
      jest.spyOn(tagTypeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tagType: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tagType }));
      saveSubject.complete();

      // THEN
      expect(tagTypeFormService.getTagType).toHaveBeenCalled();
      expect(tagTypeService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITagType>>();
      const tagType = { id: 123 };
      jest.spyOn(tagTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tagType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(tagTypeService.update).toHaveBeenCalled();
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
  });
});
