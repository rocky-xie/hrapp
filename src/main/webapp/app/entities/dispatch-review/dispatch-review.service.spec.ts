import { beforeEach, describe, expect, it, vi } from 'vitest';

import axios from 'axios';
import dayjs from 'dayjs';

import { DATE_FORMAT, DATE_TIME_FORMAT } from '@/shared/composables/date-format';
import { DispatchReview } from '@/shared/model/dispatch-review.model';

import DispatchReviewService from './dispatch-review.service';

const error = {
  response: {
    status: null,
    data: {
      type: null,
    },
  },
};

const axiosStub = {
  get: vi.spyOn(axios, 'get'),
  post: vi.spyOn(axios, 'post'),
  put: vi.spyOn(axios, 'put'),
  patch: vi.spyOn(axios, 'patch'),
  delete: vi.spyOn(axios, 'delete'),
};

describe('Service Tests', () => {
  describe('DispatchReview Service', () => {
    let service: DispatchReviewService;
    let elemDefault;
    let currentDate: Date;

    beforeEach(() => {
      service = new DispatchReviewService();
      currentDate = new Date();
      elemDefault = new DispatchReview(123, currentDate, 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', currentDate, 'AAAAAAA');
    });

    describe('Service methods', () => {
      it('should find an element', async () => {
        const returnedFromService = {
          reviewedAt: dayjs(currentDate).format(DATE_TIME_FORMAT),
          extensionEndDate: dayjs(currentDate).format(DATE_FORMAT),
          ...elemDefault,
        };
        axiosStub.get.mockResolvedValue({ data: returnedFromService });

        return service.find(123).then(res => {
          expect(res).toMatchObject(elemDefault);
        });
      });

      it('should not find an element', async () => {
        axiosStub.get.mockRejectedValue(error);
        return service
          .find(123)
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should create a DispatchReview', async () => {
        const returnedFromService = {
          id: 123,
          reviewedAt: dayjs(currentDate).format(DATE_TIME_FORMAT),
          extensionEndDate: dayjs(currentDate).format(DATE_FORMAT),
          ...elemDefault,
        };
        const expected = { reviewedAt: currentDate, extensionEndDate: currentDate, ...returnedFromService };

        axiosStub.post.mockResolvedValue({ data: returnedFromService });
        return service.create({}).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not create a DispatchReview', async () => {
        axiosStub.post.mockRejectedValue(error);

        return service
          .create({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should update a DispatchReview', async () => {
        const returnedFromService = {
          reviewedAt: dayjs(currentDate).format(DATE_TIME_FORMAT),
          reviewerUsername: 'BBBBBB',
          result: 'BBBBBB',
          recommendation: 'BBBBBB',
          extensionEndDate: dayjs(currentDate).format(DATE_FORMAT),
          finalDecision: 'BBBBBB',
          ...elemDefault,
        };

        const expected = { reviewedAt: currentDate, extensionEndDate: currentDate, ...returnedFromService };
        axiosStub.put.mockResolvedValue({ data: returnedFromService });

        return service.update(expected).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not update a DispatchReview', async () => {
        axiosStub.put.mockRejectedValue(error);

        return service
          .update({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should partial update a DispatchReview', async () => {
        const patchObject = {
          reviewerUsername: 'BBBBBB',
          result: 'BBBBBB',
          recommendation: 'BBBBBB',
          extensionEndDate: dayjs(currentDate).format(DATE_FORMAT),
          finalDecision: 'BBBBBB',
          ...new DispatchReview(),
        };
        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = { reviewedAt: currentDate, extensionEndDate: currentDate, ...returnedFromService };
        axiosStub.patch.mockResolvedValue({ data: returnedFromService });

        return service.partialUpdate(patchObject).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not partial update a DispatchReview', async () => {
        axiosStub.patch.mockRejectedValue(error);

        return service
          .partialUpdate({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should return a list of DispatchReview', async () => {
        const returnedFromService = {
          reviewedAt: dayjs(currentDate).format(DATE_TIME_FORMAT),
          reviewerUsername: 'BBBBBB',
          result: 'BBBBBB',
          recommendation: 'BBBBBB',
          extensionEndDate: dayjs(currentDate).format(DATE_FORMAT),
          finalDecision: 'BBBBBB',
          ...elemDefault,
        };
        const expected = { reviewedAt: currentDate, extensionEndDate: currentDate, ...returnedFromService };
        axiosStub.get.mockResolvedValue([returnedFromService]);
        return service.retrieve({ sort: {}, page: 0, size: 10 }).then(res => {
          expect(res).toContainEqual(expected);
        });
      });

      it('should not return a list of DispatchReview', async () => {
        axiosStub.get.mockRejectedValue(error);

        return service
          .retrieve()
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should delete a DispatchReview', async () => {
        axiosStub.delete.mockResolvedValue({ ok: true });
        return service.delete(123).then(res => {
          expect(res.ok).toBeTruthy();
        });
      });

      it('should not delete a DispatchReview', async () => {
        axiosStub.delete.mockRejectedValue(error);

        return service
          .delete(123)
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });
    });
  });
});
