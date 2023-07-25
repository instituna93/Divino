import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMemberTag } from '../member-tag.model';
import { MemberTagService } from '../service/member-tag.service';

export const memberTagResolve = (route: ActivatedRouteSnapshot): Observable<null | IMemberTag> => {
  const id = route.params['id'];
  if (id) {
    return inject(MemberTagService)
      .find(id)
      .pipe(
        mergeMap((memberTag: HttpResponse<IMemberTag>) => {
          if (memberTag.body) {
            return of(memberTag.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        })
      );
  }
  return of(null);
};

export default memberTagResolve;
