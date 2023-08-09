import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { InstrumentFormService } from './instrument-form.service';
import { InstrumentService } from '../service/instrument.service';
import { IInstrument } from '../instrument.model';

import { InstrumentUpdateComponent } from './instrument-update.component';

describe('Instrument Management Update Component', () => {
  let comp: InstrumentUpdateComponent;
  let fixture: ComponentFixture<InstrumentUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let instrumentFormService: InstrumentFormService;
  let instrumentService: InstrumentService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), InstrumentUpdateComponent],
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
      .overrideTemplate(InstrumentUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(InstrumentUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    instrumentFormService = TestBed.inject(InstrumentFormService);
    instrumentService = TestBed.inject(InstrumentService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const instrument: IInstrument = { id: 456 };

      activatedRoute.data = of({ instrument });
      comp.ngOnInit();

      expect(comp.instrument).toEqual(instrument);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInstrument>>();
      const instrument = { id: 123 };
      jest.spyOn(instrumentFormService, 'getInstrument').mockReturnValue(instrument);
      jest.spyOn(instrumentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ instrument });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: instrument }));
      saveSubject.complete();

      // THEN
      expect(instrumentFormService.getInstrument).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(instrumentService.update).toHaveBeenCalledWith(expect.objectContaining(instrument));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInstrument>>();
      const instrument = { id: 123 };
      jest.spyOn(instrumentFormService, 'getInstrument').mockReturnValue({ id: null });
      jest.spyOn(instrumentService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ instrument: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: instrument }));
      saveSubject.complete();

      // THEN
      expect(instrumentFormService.getInstrument).toHaveBeenCalled();
      expect(instrumentService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInstrument>>();
      const instrument = { id: 123 };
      jest.spyOn(instrumentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ instrument });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(instrumentService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
