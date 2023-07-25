import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TagTypeComponent } from './list/tag-type.component';
import { TagTypeDetailComponent } from './detail/tag-type-detail.component';
import { TagTypeUpdateComponent } from './update/tag-type-update.component';
import TagTypeResolve from './route/tag-type-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const tagTypeRoute: Routes = [
  {
    path: '',
    component: TagTypeComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TagTypeDetailComponent,
    resolve: {
      tagType: TagTypeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TagTypeUpdateComponent,
    resolve: {
      tagType: TagTypeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TagTypeUpdateComponent,
    resolve: {
      tagType: TagTypeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default tagTypeRoute;
