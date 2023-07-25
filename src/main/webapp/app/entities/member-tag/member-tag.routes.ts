import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { MemberTagComponent } from './list/member-tag.component';
import { MemberTagDetailComponent } from './detail/member-tag-detail.component';
import { MemberTagUpdateComponent } from './update/member-tag-update.component';
import MemberTagResolve from './route/member-tag-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const memberTagRoute: Routes = [
  {
    path: '',
    component: MemberTagComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MemberTagDetailComponent,
    resolve: {
      memberTag: MemberTagResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MemberTagUpdateComponent,
    resolve: {
      memberTag: MemberTagResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MemberTagUpdateComponent,
    resolve: {
      memberTag: MemberTagResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default memberTagRoute;
