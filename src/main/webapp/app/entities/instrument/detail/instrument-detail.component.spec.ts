import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { InstrumentDetailComponent } from './instrument-detail.component';

describe('Instrument Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [InstrumentDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: InstrumentDetailComponent,
              resolve: { instrument: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding()
        ),
      ],
    })
      .overrideTemplate(InstrumentDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load instrument on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', InstrumentDetailComponent);

      // THEN
      expect(instance.instrument).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
