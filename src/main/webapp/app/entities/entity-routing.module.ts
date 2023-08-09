import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'member',
        data: { pageTitle: 'divinoApp.member.home.title' },
        loadChildren: () => import('./member/member.routes'),
      },
      {
        path: 'member-tag',
        data: { pageTitle: 'divinoApp.memberTag.home.title' },
        loadChildren: () => import('./member-tag/member-tag.routes'),
      },
      {
        path: 'tag',
        data: { pageTitle: 'divinoApp.tag.home.title' },
        loadChildren: () => import('./tag/tag.routes'),
      },
      {
        path: 'tag-type',
        data: { pageTitle: 'divinoApp.tagType.home.title' },
        loadChildren: () => import('./tag-type/tag-type.routes'),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
