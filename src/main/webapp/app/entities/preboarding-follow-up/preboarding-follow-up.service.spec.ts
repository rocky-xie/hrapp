import { beforeEach, describe, expect, it, vi } from 'vitest';

import axios from 'axios';
import dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from '@/shared/composables/date-format';
import { PreboardingFollowUp } from '@/shared/model/preboarding-follow-up.model';

import PreboardingFollowUpService from './preboarding-follow-up.service';

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
  describe('PreboardingFollowUp Service', () => {
    let service: PreboardingFollowUpService;
    let elemDefault;
    let currentDate: Date;

    beforeEach(() => {
      service = new PreboardingFollowUpService();
      currentDate = new Date();
      elemDefault = new PreboardingFollowUp(
        123,
        0,
        0,
        currentDate,
        currentDate,
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        currentDate,
      );
    });

    describe('Service methods', () => {
      it('should find an element', async () => {
        const returnedFromService = {
          plannedAt: dayjs(currentDate).format(DATE_TIME_FORMAT),
          contactedAt: dayjs(currentDate).format(DATE_TIME_FORMAT),
          nextContactAt: dayjs(currentDate).format(DATE_TIME_FORMAT),
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

      it('should create a PreboardingFollowUp', async () => {
        const returnedFromService = {
          id: 123,
          plannedAt: dayjs(currentDate).format(DATE_TIME_FORMAT),
          contactedAt: dayjs(currentDate).format(DATE_TIME_FORMAT),
          nextContactAt: dayjs(currentDate).format(DATE_TIME_FORMAT),
          ...elemDefault,
        };
        const expected = { plannedAt: currentDate, contactedAt: currentDate, nextContactAt: currentDate, ...returnedFromService };

        axiosStub.post.mockResolvedValue({ data: returnedFromService });
        return service.create({}).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not create a PreboardingFollowUp', async () => {
        axiosStub.post.mockRejectedValue(error);

        return service
          .create({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should update a PreboardingFollowUp', async () => {
        const returnedFromService = {
          candidateId: 1,
          personId: 1,
          plannedAt: dayjs(currentDate).format(DATE_TIME_FORMAT),
          contactedAt: dayjs(currentDate).format(DATE_TIME_FORMAT),
          ownerUsername: 'BBBBBB',
          contactMethod: 'BBBBBB',
          result: 'BBBBBB',
          candidateIntent: 'BBBBBB',
          riskLevel: 'BBBBBB',
          nextAction: 'BBBBBB',
          nextContactAt: dayjs(currentDate).format(DATE_TIME_FORMAT),
          ...elemDefault,
        };

        const expected = { plannedAt: currentDate, contactedAt: currentDate, nextContactAt: currentDate, ...returnedFromService };
        axiosStub.put.mockResolvedValue({ data: returnedFromService });

        return service.update(expected).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not update a PreboardingFollowUp', async () => {
        axiosStub.put.mockRejectedValue(error);

        return service
          .update({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should partial update a PreboardingFollowUp', async () => {
        const patchObject = {
          candidateId: 1,
          plannedAt: dayjs(currentDate).format(DATE_TIME_FORMAT),
          contactMethod: 'BBBBBB',
          result: 'BBBBBB',
          riskLevel: 'BBBBBB',
          ...new PreboardingFollowUp(),
        };
        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = { plannedAt: currentDate, contactedAt: currentDate, nextContactAt: currentDate, ...returnedFromService };
        axiosStub.patch.mockResolvedValue({ data: returnedFromService });

        return service.partialUpdate(patchObject).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not partial update a PreboardingFollowUp', async () => {
        axiosStub.patch.mockRejectedValue(error);

        return service
          .partialUpdate({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should return a list of PreboardingFollowUp', async () => {
        const returnedFromService = {
          candidateId: 1,
          personId: 1,
          plannedAt: dayjs(currentDate).format(DATE_TIME_FORMAT),
          contactedAt: dayjs(currentDate).format(DATE_TIME_FORMAT),
          ownerUsername: 'BBBBBB',
          contactMethod: 'BBBBBB',
          result: 'BBBBBB',
          candidateIntent: 'BBBBBB',
          riskLevel: 'BBBBBB',
          nextAction: 'BBBBBB',
          nextContactAt: dayjs(currentDate).format(DATE_TIME_FORMAT),
          ...elemDefault,
        };
        const expected = { plannedAt: currentDate, contactedAt: currentDate, nextContactAt: currentDate, ...returnedFromService };
        axiosStub.get.mockResolvedValue([returnedFromService]);
        return service.retrieve({ sort: {}, page: 0, size: 10 }).then(res => {
          expect(res).toContainEqual(expected);
        });
      });

      it('should not return a list of PreboardingFollowUp', async () => {
        axiosStub.get.mockRejectedValue(error);

        return service
          .retrieve()
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should delete a PreboardingFollowUp', async () => {
        axiosStub.delete.mockResolvedValue({ ok: true });
        return service.delete(123).then(res => {
          expect(res.ok).toBeTruthy();
        });
      });

      it('should not delete a PreboardingFollowUp', async () => {
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
