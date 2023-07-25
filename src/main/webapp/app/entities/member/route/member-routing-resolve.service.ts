import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMember } from '../member.model';
import { MemberService } from '../service/member.service';

export const memberResolve = (route: ActivatedRouteSnapshot): Observable<null | IMember> => {
  const id = route.params['id'];
  if (id) {
    return inject(MemberService)
      .find(id)
      .pipe(
        mergeMap((member: HttpResponse<IMember>) => {
          if (member.body) {
            return of(member.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        })
      );
  }
  return of(null);
};

export default memberResolve;
