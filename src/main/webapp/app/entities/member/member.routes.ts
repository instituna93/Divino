import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { MemberComponent } from './list/member.component';
import { MemberDetailComponent } from './detail/member-detail.component';
import { MemberUpdateComponent } from './update/member-update.component';
import MemberResolve from './route/member-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const memberRoute: Routes = [
  {
    path: '',
    component: MemberComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MemberDetailComponent,
    resolve: {
      member: MemberResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MemberUpdateComponent,
    resolve: {
      member: MemberResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MemberUpdateComponent,
    resolve: {
      member: MemberResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default memberRoute;
