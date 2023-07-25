import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'member',
        data: { pageTitle: 'mngmtInstitunaApp.member.home.title' },
        loadChildren: () => import('./member/member.routes'),
      },
      {
        path: 'member-tag',
        data: { pageTitle: 'mngmtInstitunaApp.memberTag.home.title' },
        loadChildren: () => import('./member-tag/member-tag.routes'),
      },
      {
        path: 'tag',
        data: { pageTitle: 'mngmtInstitunaApp.tag.home.title' },
        loadChildren: () => import('./tag/tag.routes'),
      },
      {
        path: 'tag-type',
        data: { pageTitle: 'mngmtInstitunaApp.tagType.home.title' },
        loadChildren: () => import('./tag-type/tag-type.routes'),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
