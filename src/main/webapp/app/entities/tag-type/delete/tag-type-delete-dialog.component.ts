import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITagType } from '../tag-type.model';
import { TagTypeService } from '../service/tag-type.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  standalone: true,
  templateUrl: './tag-type-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class TagTypeDeleteDialogComponent {
  tagType?: ITagType;

  constructor(protected tagTypeService: TagTypeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.tagTypeService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
