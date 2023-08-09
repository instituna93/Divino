import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IInstrumentRequest } from '../instrument-request.model';
import { InstrumentRequestService } from '../service/instrument-request.service';

export const instrumentRequestResolve = (route: ActivatedRouteSnapshot): Observable<null | IInstrumentRequest> => {
  const id = route.params['id'];
  if (id) {
    return inject(InstrumentRequestService)
      .find(id)
      .pipe(
        mergeMap((instrumentRequest: HttpResponse<IInstrumentRequest>) => {
          if (instrumentRequest.body) {
            return of(instrumentRequest.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        })
      );
  }
  return of(null);
};

export default instrumentRequestResolve;
