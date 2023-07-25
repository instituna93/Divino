import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { IMemberTag } from '../member-tag.model';
import { MemberTagService } from '../service/member-tag.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  standalone: true,
  templateUrl: './member-tag-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class MemberTagDeleteDialogComponent {
  memberTag?: IMemberTag;

  constructor(protected memberTagService: MemberTagService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.memberTagService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
