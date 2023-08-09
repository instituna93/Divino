import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { InstrumentRequestDetailComponent } from './instrument-request-detail.component';

describe('InstrumentRequest Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [InstrumentRequestDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: InstrumentRequestDetailComponent,
              resolve: { instrumentRequest: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding()
        ),
      ],
    })
      .overrideTemplate(InstrumentRequestDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load instrumentRequest on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', InstrumentRequestDetailComponent);

      // THEN
      expect(instance.instrumentRequest).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
