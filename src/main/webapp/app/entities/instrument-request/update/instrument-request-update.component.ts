import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { InstrumentRequestFormService, InstrumentRequestFormGroup } from './instrument-request-form.service';
import { IInstrumentRequest } from '../instrument-request.model';
import { InstrumentRequestService } from '../service/instrument-request.service';
import { IInstrument } from 'app/entities/instrument/instrument.model';
import { InstrumentService } from 'app/entities/instrument/service/instrument.service';
import { IMember } from 'app/entities/member/member.model';
import { MemberService } from 'app/entities/member/service/member.service';

@Component({
  standalone: true,
  selector: 'jhi-instrument-request-update',
  templateUrl: './instrument-request-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class InstrumentRequestUpdateComponent implements OnInit {
  isSaving = false;
  instrumentRequest: IInstrumentRequest | null = null;

  instrumentsSharedCollection: IInstrument[] = [];
  membersSharedCollection: IMember[] = [];

  editForm: InstrumentRequestFormGroup = this.instrumentRequestFormService.createInstrumentRequestFormGroup();

  constructor(
    protected instrumentRequestService: InstrumentRequestService,
    protected instrumentRequestFormService: InstrumentRequestFormService,
    protected instrumentService: InstrumentService,
    protected memberService: MemberService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareInstrument = (o1: IInstrument | null, o2: IInstrument | null): boolean => this.instrumentService.compareInstrument(o1, o2);

  compareMember = (o1: IMember | null, o2: IMember | null): boolean => this.memberService.compareMember(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ instrumentRequest }) => {
      this.instrumentRequest = instrumentRequest;
      if (instrumentRequest) {
        this.updateForm(instrumentRequest);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const instrumentRequest = this.instrumentRequestFormService.getInstrumentRequest(this.editForm);
    if (instrumentRequest.id !== null) {
      this.subscribeToSaveResponse(this.instrumentRequestService.update(instrumentRequest));
    } else {
      this.subscribeToSaveResponse(this.instrumentRequestService.create(instrumentRequest));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IInstrumentRequest>>): void {
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

  protected updateForm(instrumentRequest: IInstrumentRequest): void {
    this.instrumentRequest = instrumentRequest;
    this.instrumentRequestFormService.resetForm(this.editForm, instrumentRequest);

    this.instrumentsSharedCollection = this.instrumentService.addInstrumentToCollectionIfMissing<IInstrument>(
      this.instrumentsSharedCollection,
      instrumentRequest.instrument
    );
    this.membersSharedCollection = this.memberService.addMemberToCollectionIfMissing<IMember>(
      this.membersSharedCollection,
      instrumentRequest.member
    );
  }

  protected loadRelationshipsOptions(): void {
    this.instrumentService
      .query()
      .pipe(map((res: HttpResponse<IInstrument[]>) => res.body ?? []))
      .pipe(
        map((instruments: IInstrument[]) =>
          this.instrumentService.addInstrumentToCollectionIfMissing<IInstrument>(instruments, this.instrumentRequest?.instrument)
        )
      )
      .subscribe((instruments: IInstrument[]) => (this.instrumentsSharedCollection = instruments));

    this.memberService
      .query()
      .pipe(map((res: HttpResponse<IMember[]>) => res.body ?? []))
      .pipe(
        map((members: IMember[]) => this.memberService.addMemberToCollectionIfMissing<IMember>(members, this.instrumentRequest?.member))
      )
      .subscribe((members: IMember[]) => (this.membersSharedCollection = members));
  }
}
