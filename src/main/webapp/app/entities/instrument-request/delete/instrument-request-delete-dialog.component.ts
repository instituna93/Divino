import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { IInstrumentRequest } from '../instrument-request.model';
import { InstrumentRequestService } from '../service/instrument-request.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  standalone: true,
  templateUrl: './instrument-request-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class InstrumentRequestDeleteDialogComponent {
  instrumentRequest?: IInstrumentRequest;

  constructor(protected instrumentRequestService: InstrumentRequestService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.instrumentRequestService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
