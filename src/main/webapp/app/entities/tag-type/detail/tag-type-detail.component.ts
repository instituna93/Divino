import { Component, Input } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import HasAnyAuthorityDirective from 'app/shared/auth/has-any-authority.directive';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { ITagType } from '../tag-type.model';

@Component({
  standalone: true,
  selector: 'jhi-tag-type-detail',
  templateUrl: './tag-type-detail.component.html',
  imports: [RouterModule, SharedModule, HasAnyAuthorityDirective, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class TagTypeDetailComponent {
  @Input() tagType: ITagType | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  previousState(): void {
    window.history.back();
  }
}
