import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { InstrumentComponent } from './list/instrument.component';
import { InstrumentDetailComponent } from './detail/instrument-detail.component';
import { InstrumentUpdateComponent } from './update/instrument-update.component';
import InstrumentResolve from './route/instrument-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const instrumentRoute: Routes = [
  {
    path: '',
    component: InstrumentComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: InstrumentDetailComponent,
    resolve: {
      instrument: InstrumentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: InstrumentUpdateComponent,
    resolve: {
      instrument: InstrumentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: InstrumentUpdateComponent,
    resolve: {
      instrument: InstrumentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default instrumentRoute;
