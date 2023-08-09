import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { InstrumentFormService, InstrumentFormGroup } from './instrument-form.service';
import { IInstrument } from '../instrument.model';
import { InstrumentService } from '../service/instrument.service';
import { InstrumentType } from 'app/entities/enumerations/instrument-type.model';

@Component({
  standalone: true,
  selector: 'jhi-instrument-update',
  templateUrl: './instrument-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class InstrumentUpdateComponent implements OnInit {
  isSaving = false;
  instrument: IInstrument | null = null;
  instrumentTypeValues = Object.keys(InstrumentType);

  editForm: InstrumentFormGroup = this.instrumentFormService.createInstrumentFormGroup();

  constructor(
    protected instrumentService: InstrumentService,
    protected instrumentFormService: InstrumentFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ instrument }) => {
      this.instrument = instrument;
      if (instrument) {
        this.updateForm(instrument);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const instrument = this.instrumentFormService.getInstrument(this.editForm);
    if (instrument.id !== null) {
      this.subscribeToSaveResponse(this.instrumentService.update(instrument));
    } else {
      this.subscribeToSaveResponse(this.instrumentService.create(instrument));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IInstrument>>): void {
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

  protected updateForm(instrument: IInstrument): void {
    this.instrument = instrument;
    this.instrumentFormService.resetForm(this.editForm, instrument);
  }
}
