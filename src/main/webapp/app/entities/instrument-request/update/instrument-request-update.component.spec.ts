import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { InstrumentRequestFormService } from './instrument-request-form.service';
import { InstrumentRequestService } from '../service/instrument-request.service';
import { IInstrumentRequest } from '../instrument-request.model';
import { IInstrument } from 'app/entities/instrument/instrument.model';
import { InstrumentService } from 'app/entities/instrument/service/instrument.service';
import { IMember } from 'app/entities/member/member.model';
import { MemberService } from 'app/entities/member/service/member.service';

import { InstrumentRequestUpdateComponent } from './instrument-request-update.component';

describe('InstrumentRequest Management Update Component', () => {
  let comp: InstrumentRequestUpdateComponent;
  let fixture: ComponentFixture<InstrumentRequestUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let instrumentRequestFormService: InstrumentRequestFormService;
  let instrumentRequestService: InstrumentRequestService;
  let instrumentService: InstrumentService;
  let memberService: MemberService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), InstrumentRequestUpdateComponent],
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
      .overrideTemplate(InstrumentRequestUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(InstrumentRequestUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    instrumentRequestFormService = TestBed.inject(InstrumentRequestFormService);
    instrumentRequestService = TestBed.inject(InstrumentRequestService);
    instrumentService = TestBed.inject(InstrumentService);
    memberService = TestBed.inject(MemberService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Instrument query and add missing value', () => {
      const instrumentRequest: IInstrumentRequest = { id: 456 };
      const instrument: IInstrument = { id: 2717 };
      instrumentRequest.instrument = instrument;

      const instrumentCollection: IInstrument[] = [{ id: 9574 }];
      jest.spyOn(instrumentService, 'query').mockReturnValue(of(new HttpResponse({ body: instrumentCollection })));
      const additionalInstruments = [instrument];
      const expectedCollection: IInstrument[] = [...additionalInstruments, ...instrumentCollection];
      jest.spyOn(instrumentService, 'addInstrumentToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ instrumentRequest });
      comp.ngOnInit();

      expect(instrumentService.query).toHaveBeenCalled();
      expect(instrumentService.addInstrumentToCollectionIfMissing).toHaveBeenCalledWith(
        instrumentCollection,
        ...additionalInstruments.map(expect.objectContaining)
      );
      expect(comp.instrumentsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Member query and add missing value', () => {
      const instrumentRequest: IInstrumentRequest = { id: 456 };
      const member: IMember = { id: 12923 };
      instrumentRequest.member = member;

      const memberCollection: IMember[] = [{ id: 18274 }];
      jest.spyOn(memberService, 'query').mockReturnValue(of(new HttpResponse({ body: memberCollection })));
      const additionalMembers = [member];
      const expectedCollection: IMember[] = [...additionalMembers, ...memberCollection];
      jest.spyOn(memberService, 'addMemberToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ instrumentRequest });
      comp.ngOnInit();

      expect(memberService.query).toHaveBeenCalled();
      expect(memberService.addMemberToCollectionIfMissing).toHaveBeenCalledWith(
        memberCollection,
        ...additionalMembers.map(expect.objectContaining)
      );
      expect(comp.membersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const instrumentRequest: IInstrumentRequest = { id: 456 };
      const instrument: IInstrument = { id: 24359 };
      instrumentRequest.instrument = instrument;
      const member: IMember = { id: 20264 };
      instrumentRequest.member = member;

      activatedRoute.data = of({ instrumentRequest });
      comp.ngOnInit();

      expect(comp.instrumentsSharedCollection).toContain(instrument);
      expect(comp.membersSharedCollection).toContain(member);
      expect(comp.instrumentRequest).toEqual(instrumentRequest);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInstrumentRequest>>();
      const instrumentRequest = { id: 123 };
      jest.spyOn(instrumentRequestFormService, 'getInstrumentRequest').mockReturnValue(instrumentRequest);
      jest.spyOn(instrumentRequestService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ instrumentRequest });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: instrumentRequest }));
      saveSubject.complete();

      // THEN
      expect(instrumentRequestFormService.getInstrumentRequest).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(instrumentRequestService.update).toHaveBeenCalledWith(expect.objectContaining(instrumentRequest));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInstrumentRequest>>();
      const instrumentRequest = { id: 123 };
      jest.spyOn(instrumentRequestFormService, 'getInstrumentRequest').mockReturnValue({ id: null });
      jest.spyOn(instrumentRequestService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ instrumentRequest: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: instrumentRequest }));
      saveSubject.complete();

      // THEN
      expect(instrumentRequestFormService.getInstrumentRequest).toHaveBeenCalled();
      expect(instrumentRequestService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInstrumentRequest>>();
      const instrumentRequest = { id: 123 };
      jest.spyOn(instrumentRequestService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ instrumentRequest });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(instrumentRequestService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareInstrument', () => {
      it('Should forward to instrumentService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(instrumentService, 'compareInstrument');
        comp.compareInstrument(entity, entity2);
        expect(instrumentService.compareInstrument).toHaveBeenCalledWith(entity, entity2);
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
