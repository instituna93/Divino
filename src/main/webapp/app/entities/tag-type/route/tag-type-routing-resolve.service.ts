import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITagType } from '../tag-type.model';
import { TagTypeService } from '../service/tag-type.service';

export const tagTypeResolve = (route: ActivatedRouteSnapshot): Observable<null | ITagType> => {
  const id = route.params['id'];
  if (id) {
    return inject(TagTypeService)
      .find(id)
      .pipe(
        mergeMap((tagType: HttpResponse<ITagType>) => {
          if (tagType.body) {
            return of(tagType.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        })
      );
  }
  return of(null);
};

export default tagTypeResolve;
