jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { MemberTagService } from '../service/member-tag.service';

import { MemberTagDeleteDialogComponent } from './member-tag-delete-dialog.component';

describe('MemberTag Management Delete Component', () => {
  let comp: MemberTagDeleteDialogComponent;
  let fixture: ComponentFixture<MemberTagDeleteDialogComponent>;
  let service: MemberTagService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, MemberTagDeleteDialogComponent],
      providers: [NgbActiveModal],
    })
      .overrideTemplate(MemberTagDeleteDialogComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(MemberTagDeleteDialogComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(MemberTagService);
    mockActiveModal = TestBed.inject(NgbActiveModal);
  });

  describe('confirmDelete', () => {
    it('Should call delete service on confirmDelete', inject(
      [],
      fakeAsync(() => {
        // GIVEN
        jest.spyOn(service, 'delete').mockReturnValue(of(new HttpResponse({ body: {} })));

        // WHEN
        comp.confirmDelete(123);
        tick();

        // THEN
        expect(service.delete).toHaveBeenCalledWith(123);
        expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
      })
    ));

    it('Should not call delete service on clear', () => {
      // GIVEN
      jest.spyOn(service, 'delete');

      // WHEN
      comp.cancel();

      // THEN
      expect(service.delete).not.toHaveBeenCalled();
      expect(mockActiveModal.close).not.toHaveBeenCalled();
      expect(mockActiveModal.dismiss).toHaveBeenCalled();
    });
  });
});
