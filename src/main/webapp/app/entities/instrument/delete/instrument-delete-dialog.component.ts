import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { IInstrument } from '../instrument.model';
import { InstrumentService } from '../service/instrument.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  standalone: true,
  templateUrl: './instrument-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class InstrumentDeleteDialogComponent {
  instrument?: IInstrument;

  constructor(protected instrumentService: InstrumentService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.instrumentService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
