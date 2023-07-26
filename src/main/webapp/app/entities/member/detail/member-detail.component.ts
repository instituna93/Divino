import { Component, Input } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import HasAnyAuthorityDirective from 'app/shared/auth/has-any-authority.directive';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IMember } from '../member.model';

@Component({
  standalone: true,
  selector: 'jhi-member-detail',
  templateUrl: './member-detail.component.html',
  imports: [RouterModule, SharedModule, HasAnyAuthorityDirective, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class MemberDetailComponent {
  @Input() member: IMember | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  previousState(): void {
    window.history.back();
  }
}
