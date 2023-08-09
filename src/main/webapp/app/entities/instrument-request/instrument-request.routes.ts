import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { InstrumentRequestComponent } from './list/instrument-request.component';
import { InstrumentRequestDetailComponent } from './detail/instrument-request-detail.component';
import { InstrumentRequestUpdateComponent } from './update/instrument-request-update.component';
import InstrumentRequestResolve from './route/instrument-request-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const instrumentRequestRoute: Routes = [
  {
    path: '',
    component: InstrumentRequestComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: InstrumentRequestDetailComponent,
    resolve: {
      instrumentRequest: InstrumentRequestResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: InstrumentRequestUpdateComponent,
    resolve: {
      instrumentRequest: InstrumentRequestResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: InstrumentRequestUpdateComponent,
    resolve: {
      instrumentRequest: InstrumentRequestResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default instrumentRequestRoute;
