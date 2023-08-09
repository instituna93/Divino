import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IInstrument } from '../instrument.model';
import { InstrumentService } from '../service/instrument.service';

export const instrumentResolve = (route: ActivatedRouteSnapshot): Observable<null | IInstrument> => {
  const id = route.params['id'];
  if (id) {
    return inject(InstrumentService)
      .find(id)
      .pipe(
        mergeMap((instrument: HttpResponse<IInstrument>) => {
          if (instrument.body) {
            return of(instrument.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        })
      );
  }
  return of(null);
};

export default instrumentResolve;
