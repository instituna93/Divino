import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { MemberTagDetailComponent } from './member-tag-detail.component';

describe('MemberTag Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MemberTagDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: MemberTagDetailComponent,
              resolve: { memberTag: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding()
        ),
      ],
    })
      .overrideTemplate(MemberTagDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load memberTag on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', MemberTagDetailComponent);

      // THEN
      expect(instance.memberTag).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
